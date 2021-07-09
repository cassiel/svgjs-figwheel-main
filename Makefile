STAGING=./_STAGING

.PHONY: lein_build
lein_build:
	lein clean
	lein fig:ws

.PHONY: stage
stage: lein_build
	rm -rf $(STAGING)
	mkdir $(STAGING)
	cp -r resources/public/* $(STAGING)/
	cp -r target/public/cljs-out $(STAGING)/

	cd $(STAGING)/cljs-out \
		&& sed -e 's|/cljs-out|cljs-out|' <dev-main.js > _tmp.js \
		&& rm dev-main.js \
		&& mv _tmp.js dev-main.js
