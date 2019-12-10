#!/bin/sh
#
#    dPOS Dongwun Point Of Sale designed for Touch Screen
#    Copyright (c) 2009-2017 Dongwun & uniCenta & previous Openbravo POS works
#    http://www.dongwun.com
#
#    This file is part of dPOS.
#
#    dPOS is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    dPOS is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with dPOS.  If not, see <http://www.gnu.org/licenses/>.
# Linux script to start migrate of unicentaopos
#
DIRNAME=`dirname $0`
CP=$DIRNAME/unicentaopos.jar
CP=$CP:$DIRNAME/libjasperreports-4.5.1.jar
CP=$CP:$DIRNAME/lib/jcommon-1.0.15.jar
CP=$CP:$DIRNAME/lib/jfreechart-1.0.12.jar
CP=$CP:$DIRNAME/lib/jdt-compiler-3.1.1.jar
CP=$CP:$DIRNAME/lib/commons-beanutils-1.8.3.jar
CP=$CP:$DIRNAME/lib/commons-digester-2.1.jar
CP=$CP:$DIRNAME/lib/iText-2.1.7.jar
CP=$CP:$DIRNAME/lib/poi-3.8-20120326.jar
CP=$CP:$DIRNAME/lib/barcode4j-2.0.jar
CP=$CP:$DIRNAME/lib/commons-codec-1.4.jar
CP=$CP:$DIRNAME/lib/velocity-1.7-dep.jar
CP=$CP:$DIRNAME/lib/commons-collections-3.2.1.jar
CP=$CP:$DIRNAME/lib/commons-lang-2.4.jar
CP=$CP:$DIRNAME/lib/bsh-core-2.0b4.jar
CP=$CP:$DIRNAME/lib/RXTXcomm.jar
CP=$CP:$DIRNAME/lib/jpos1121.jar
CP=$CP:$DIRNAME/lib/swingx-all-1.6.4.jar
CP=$CP:$DIRNAME/lib/substance.jar
CP=$CP:$DIRNAME/lib/substance-swingx.jar

# Apache Axis SOAP libraries.
CP=$CP:$DIRNAME/lib/axis.jar
CP=$CP:$DIRNAME/lib/jaxrpc.jar
CP=$CP:$DIRNAME/lib/saaj.jar
CP=$CP:$DIRNAME/lib/wsdl4j-1.5.1.jar
CP=$CP:$DIRNAME/lib/commons-discovery-0.4.jar
CP=$CP:$DIRNAME/lib/commons-logging-1.1.jar
CP=$CP:$DIRNAME/locales/
CP=$CP:$DIRNAME/reports/


java -cp $CP -Djava.library.path=$DIRNAME/lib/Linux -Ddirname.path="$DIRNAME" com.openbravo.pos.migrate.JFrmMigrate $1
