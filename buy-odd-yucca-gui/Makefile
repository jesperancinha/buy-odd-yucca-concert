include ../Makefile
update:
	curl --compressed -o- -L https://yarnpkg.com/install.sh | bash
	npm install -g npm-check-updates
	npx browserslist --update-db && ncu -u && yarn

# Migration to react 19: https://react.dev/blog/2024/04/25/react-19-upgrade-guide
migrate:
	yarn add --exact react@^19.0.0 react-dom@^19.0.0; \
	yarn add --exact @types/react@^19.0.0 @types/react-dom@^19.0.0; \
	npx codemod@latest react/19/migration-recipe;
	npx codemod@latest react/prop-types-typescript
	npx codemod@latest react/19/replace-string-ref
	npx codemod@latest react/19/replace-act-import
