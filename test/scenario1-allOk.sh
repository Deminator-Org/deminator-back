#!/bin/bash

# Get index
echo "TEST 1: Get index"
curl -H "Content-Type: application/json" -X GET localhost:8081
echo -e "\n\n"

# Create new game
echo "TEST 2: Create new game"
curl -H "Content-Type: application/json" -X POST localhost:8081/gameSetup -d '{"playerName":"testt", "settings":{"width":10, "height":11, "mines":12, "seed":42}}'
echo -e "\n\n"

# For this seed, mines are: ["0,4","0,8","7,7","9,6","2,7","0,6","6,9","3,8","8,5","3,9","5,2","6,0"]

# Try reveal a cell (not a mine)
echo "TEST 3: Reveal a cell"
curl -H "Content-Type: application/json" -X POST localhost:8081/reveal -d '{"playerName": "testt", "coord":"5,6"}'
echo -e "\n\n"

# Try reveal a 2nd cell (a mine)
echo "TEST 3: Reveal a 2nd cell"
curl -H "Content-Type: application/json" -X POST localhost:8081/reveal -d '{"playerName": "testt", "coord":"3,8"}'
echo -e "\n\n"
