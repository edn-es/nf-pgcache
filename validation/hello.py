#!/usr/bin/env python3
import sys;

if sys.argv[1] not in ['Hola','Ciao','Bonjour']:
    sys.exit(-1)

print ("Hello %s" % sys.argv[1])
