#!/bin/bash
echo "Make sure you've started with . ./node.sh"
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"
nvm install 24.19.0
nvm use 24.19.0
