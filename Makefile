PACKAGES = \
	rescore \

NODOC_PACKAGES = \

JARS = \

JARS_3RDPARTY = \
	h2-1.3.150.jar \
	log4j-1.2.16.jar \

MAIN_CLASS     = Main
MAIN_PACKAGE   = rescore
MAIN_JAR       = 

RUN_PARAMETERS = jdbc:h2:~/test sa "" logger.conf


#*********************************************************************
#
# Javadoc
#
#*********************************************************************

WINDOWTITLE = ''
DOCTITLE    = ''
HEADER      = ''
BOTTOM      = ''

include $(JAVA_DEV_ROOT)/make/Makefile
