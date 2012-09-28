#DESTDIR = /
#PREFIX  = /
 
all:
	ant -f build.ant.xml jar

install: all
	ant -f build.ant.xml -Dprefix=$(DESTDIR)/$(PREFIX) install

dist:
	pwd
	ant -f build.ant.xml dist
	
check:
	$(MAKE) -C tests
	@echo
	@echo "Warning: only tests in tests/ were executed. JUnit-based tests in test/ and WebDSL tests in tests2/ were ignored."
