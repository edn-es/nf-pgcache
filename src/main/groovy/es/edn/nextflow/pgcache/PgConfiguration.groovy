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

import groovy.transform.PackageScope

@PackageScope
class PgConfiguration {

    final private String host
    final private Integer port
    final private String database
    final private String user
    final private String password

    PgConfiguration(Map map){
        def config = map ?: Collections.emptyMap()
        this.host = config.host?.toString()
        this.database = config.database?.toString()
        this.user = config.user?.toString()
        this.password = config.password?.toString()
        this.port = (config.port ?: 5432) as int
    }

    String getHost() {
        return host
    }

    Integer getPort() {
        return port
    }

    String getDatabase() {
        return database
    }

    String getUser() {
        return user
    }

    String getPassword() {
        return password
    }
}
