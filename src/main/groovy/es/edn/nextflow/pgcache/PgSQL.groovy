/*
 * MIT License
 *
 * Copyright (c) 2025 EDN
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package es.edn.nextflow.pgcache

import groovy.sql.Sql
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import nextflow.exception.AbortOperationException

@CompileStatic
@Slf4j
class PgSQL {

    private final PgConfiguration configuration

    private Sql sql

    PgSQL(PgConfiguration configuration) {
        this.configuration = configuration
        Class.forName("org.postgresql.Driver")
    }

    void validateConnection(){
        String url = "jdbc:postgresql://${configuration.host}:${configuration.port}/${configuration.database}"
        try{
            sql = Sql.newInstance(url, configuration.user, configuration.password, "org.postgresql.Driver")
            sql.rows("select 1")
        }catch(Exception e){
            log.error "Error validating cache connection",e
            throw new AbortOperationException("Invalid connection for nf-pqcache")
        }
    }

    void createTablesIfRequired(){
        try{
            sql.execute this.getClass().getResourceAsStream("/create-index.sql").text
            sql.execute this.getClass().getResourceAsStream("/create-cache.sql").text
        }catch(Exception e){
            log.error "Error creating tables",e
            throw new AbortOperationException("Invalid connection for nf-pqcache")
        }
    }

    byte[] readCache(String session, long id){
        def row = sql.firstRow(this.getClass().getResourceAsStream("/select-cache.sql").text, [
                id:id,
                session_id:session
        ])
        row?.entry as byte[]
    }

    void writeCache(String session, long id, byte[]entry){
        sql.executeUpdate(this.getClass().getResourceAsStream("/insert-cache.sql").text, [
                id:id,
                session_id:session,
                entry:entry
        ])
    }

    void deleteCache(String session, long id){
        sql.executeUpdate(this.getClass().getResourceAsStream("/delete-cache.sql").text, [
                id:id,
                session_id:session
        ])
    }

    void writeIndex(long key, String session, String name, boolean cached) {
        sql.executeUpdate(this.getClass().getResourceAsStream("/insert-index.sql").text, [
                id:key, cached:cached, session_id:session, name:name
        ])
    }

    void deleteIndex(String session, String name){
        sql.executeUpdate(this.getClass().getResourceAsStream("/truncate-index.sql").text,[
                session_id:session, name:name
        ])
    }

    List<Map>allIndex(String session, String name){
        def rows = sql.rows(this.getClass().getResourceAsStream("/select-index.sql").text,[
                session_id:session, name:name
        ])
        rows.inject([],{ list, row->
            list << [key:row.id, cached:row.cached]
        }) as List<Map>
    }

    void truncateTables(){
        try{
            sql.execute this.getClass().getResourceAsStream("/truncate-index.sql").text
            sql.execute this.getClass().getResourceAsStream("/truncate-cache.sql").text
        }catch(Exception e){
            log.error "Error truncating tables",e
            throw new AbortOperationException("Invalid connection for nf-pqcache")
        }
    }
}
