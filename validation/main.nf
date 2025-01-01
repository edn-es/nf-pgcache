#!/usr/bin/env nextflow

process sayHello {
  input:
  val x
  output:
  stdout
  script:
  """
    python3 /home/jorge/nextflow/nf-pgcache/validation/hello.py "$x"   
  """
}

workflow {
  Channel.of('Bonjour', 'Ciao', 'Hello', 'Hola') | sayHello | view
}