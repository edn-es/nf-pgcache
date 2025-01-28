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

import com.beust.jcommander.Parameter
import groovy.transform.CompileStatic

@CompileStatic
class PgLog{

    @Parameter(names = ['--s'], description='Character used to separate column values')
    String sep = '\\t'

    @Parameter(names=['--f','--fields'], description = 'Comma separated list of fields to include in the printed log -- Use the `-l` option to show the list of available fields')
    String fields

    @Parameter(names = ['--t','--template'], description = 'Text template used to each record in the log ')
    String templateStr

    @Parameter(names=['--l','--list-fields'], description = 'Show all available fields', arity = 0)
    boolean listFields

    @Parameter(names=['--F','--filter'], description = "Filter log entries by a custom expression e.g. process =~ /foo.*/ && status == 'COMPLETED'")
    String filterStr

    @Parameter(names='--after', description = 'Show log entries for runs executed after the specified one')
    String after

    @Parameter(names='--before', description = 'Show log entries for runs executed before the specified one')
    String before

    @Parameter(names='--but', description = 'Show log entries of all runs except the specified one')
    String but

    @Parameter(names=['--q','--quiet'], description = 'Show only run names', arity = 0)
    boolean quiet

    @Parameter(description = 'Run name or session id')
    List<String> args

    /**
     * Implements the `log` command
     */
    void run() {
        LogWrapper wrapper = new LogWrapper(
                sep:sep,
                fields:fields,
                templateStr:templateStr,
                listFields:listFields,
                filterStr:filterStr,
                after:after,
                before:before,
                but:but,
                quiet:quiet,
                args:args
        )
        wrapper.run()
    }

}
