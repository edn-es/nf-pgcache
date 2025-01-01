#!/usr/bin/env python3
import sys;
import time;

time.sleep(10)

if sys.argv[1] not in ['Hola','Ciao','Bonjour']:
    sys.exit(-1)

print ("Hello %s" % sys.argv[1])
