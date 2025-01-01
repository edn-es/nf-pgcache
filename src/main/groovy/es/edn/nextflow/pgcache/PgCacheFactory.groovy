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

import groovy.transform.CompileStatic
import nextflow.Global
import nextflow.Session
import nextflow.cache.CacheDB
import nextflow.cache.CacheFactory
import nextflow.exception.AbortOperationException
import nextflow.plugin.Priority

import java.nio.file.Path

@CompileStatic
@Priority(-5)
class PgCacheFactory extends CacheFactory{

    @Override
    protected CacheDB newInstance(UUID uniqueId, String runName, Path home) {
        if( !uniqueId ) throw new AbortOperationException("Missing cache `uuid`")
        if( !runName ) throw new AbortOperationException("Missing cache `runName`")

        def config = (Global.session as Session).config?.pgcache as Map
        def pgConfig = new PgConfiguration(config)
        final store = new PgCacheStore(uniqueId, runName, pgConfig)
        return new CacheDB(store)
    }
}
