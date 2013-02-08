all:
	@rm -f *.js 
	@ant 
	@cp war/jssglrWorker/*.cache.js ./jssglr_tmp.js
	@cat javascript/prepend.js jssglr_tmp.js javascript/append.js > jssglr.js
	@rm -f jssglr_tmp.js
	@echo Done!
