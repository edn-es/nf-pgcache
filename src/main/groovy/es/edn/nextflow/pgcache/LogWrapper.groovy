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
import nextflow.cli.CmdLog
import nextflow.trace.TraceRecord
import nextflow.ui.TableBuilder

@CompileStatic
class LogWrapper extends CmdLog{

    private List<String> ALL_FIELDS

    private boolean showHistory

    void init(){
        super.init()
        // when no CLI options have been specified, just show the history log
        showHistory = !args && !before && !after && !but

        ALL_FIELDS = []
        ALL_FIELDS.addAll( TraceRecord.FIELDS.keySet().collect { it.startsWith('%') ? 'p'+it.substring(1) : it } )
        ALL_FIELDS << 'stdout'
        ALL_FIELDS << 'stderr'
        ALL_FIELDS << 'log'
        ALL_FIELDS.sort(true)
    }

    private void printQuiet() {
        history.eachRow { List row -> println(row[2]) }
    }

    private void printHistory() {
        def table = new TableBuilder(cellSeparator: '\t')
                .head('TIMESTAMP')
                .head('DURATION')
                .head('RUN NAME')
                .head('STATUS')
                .head('REVISION ID')
                .head('SESSION ID')
                .head('COMMAND')

        history.eachRow { List<String> row ->
            row[4] = row[4].size()>10 ? row[4].substring(0,10) : row[4]
            table.append(row)
        }

        println table.toString()
    }

    @Override
    void run() {
        init()

        // -- show the list of expected fields and exit
        if( listFields ) {
            ALL_FIELDS.each { println "  $it" }
            return
        }

        // -- show the current history and exit
        if( showHistory ) {
            quiet ? printQuiet() : printHistory()
            return
        }

        // -- main
        listIds().each { entry ->

            cacheFor(entry)
                    .openForRead()
                    .eachRecord(this.&printRecord)
                    .close()

        }
    }
}
