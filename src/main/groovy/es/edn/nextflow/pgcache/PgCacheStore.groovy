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

import com.google.common.hash.HashCode
import groovy.transform.CompileStatic
import nextflow.cache.CacheStore

@CompileStatic
class PgCacheStore implements CacheStore{

    /** The session UUID */
    private final UUID uniqueId

    /** The unique run name associated with this cache instance */
    private final String runName

    private final PgConfiguration configuration

    private final PgSQL pgSQL

    PgCacheStore(UUID uniqueId, String runName, PgConfiguration configuration) {
        this.uniqueId = uniqueId
        this.runName = runName
        this.configuration = configuration
        this.pgSQL = new PgSQL(configuration)
    }

    @Override
    CacheStore open() {
        this.pgSQL.validateConnection()
        this.pgSQL.createTablesIfRequired()
        this
    }

    @Override
    CacheStore openForRead() {
        this.pgSQL.validateConnection()
        this.pgSQL.createTablesIfRequired()
        this
    }

    @Override
    void close() {
        //do nothing
    }

    @Override
    void drop() {
        pgSQL.truncateTables()
    }

    @Override
    byte[] getEntry(HashCode key) {
        pgSQL.readCache(uniqueId.toString(), key.asLong())
    }

    @Override
    void putEntry(HashCode key, byte ... value) {
        pgSQL.writeCache(uniqueId.toString(), key.asLong(), value)
    }

    @Override
    void deleteEntry(HashCode key) {
        pgSQL.deleteCache(uniqueId.toString(), key.asLong())
    }

    @Override
    void writeIndex(HashCode key, boolean cached) {
        pgSQL.writeIndex(key.asLong(), uniqueId.toString(), runName, cached)
    }

    @Override
    Iterator<Index> iterateIndex() {
        List<Index> list = pgSQL.allIndex(uniqueId.toString(), runName).collect{
            new Index( HashCode.fromLong(it.key as long), it.cached as boolean)
        } as List<Index>
        list.iterator()
    }

    @Override
    void deleteIndex() {
        pgSQL.deleteIndex(uniqueId.toString(), runName)
    }
}
