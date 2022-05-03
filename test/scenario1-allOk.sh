#!/bin/bash

# Get index
echo "TEST 1: Get index"
curl -H "Content-Type: application/json" -X GET localhost:8081/
echo -e "\n\n"

# Create new game
echo "TEST 2: Create new game"
curl -H "Content-Type: application/json" -X POST -d '{"playerName": "testt", "settings":{"width":14, "height":10, "mines":42}}' localhost:8081/gameSetup
echo -e "\n\n"
