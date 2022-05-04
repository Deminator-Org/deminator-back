#!/bin/bash

URL='localhost:8081'
JSON='Content-Type:application/json'

echo "Get index"
curl -H ${JSON} -X GET ${URL}
echo -e "\n\n"

sleep 1

echo "Create new game"
curl -H ${JSON} -X POST ${URL}/gameSetup -d '{"width":10, "height":11, "mines":12, "turnDuration": 10, "startTimeout": 0,"seed":42}'
echo -e "\n\n"

# For this seed, mines are: ["0,4","0,8","7,7","9,6","2,7","0,6","6,9","3,8","8,5","3,9","5,2","6,0"]

sleep 1

echo "Register player"
curl -H ${JSON} -X POST ${URL}/setPlayer -d '{"id": "testt", "name":"testt"}'
echo -e "\n\n"

sleep 1

echo "Reveal a cell without a mine nor a 0"
curl -H ${JSON} -X POST ${URL}/reveal -d '{"id": "testt", "coord":"5,3"}'
echo -e "\n\n"

sleep 1

echo "Rename the player"
curl -H ${JSON} -X POST ${URL}/setPlayer -d '{"id": "testt", "name":"testtttt"}'
echo -e "\n\n"

# Wait for next turn before to play
sleep 10

echo "Reveal a mine"
curl -H ${JSON} -X POST ${URL}/reveal -d '{"id": "testt", "coord":"0,4"}'
echo -e "\n\n"

# Revealed a mine, no need to wait for next turn
sleep 1

echo "Reveal a cell with 0 neighbor mines"
curl -H ${JSON} -X POST ${URL}/reveal -d '{"id": "testt", "coord":"5,5"}'
echo -e "\n\n"

# Wait for next turn before to play
sleep 11

echo "Reveal all other mines"
curl -H ${JSON} -X POST ${URL}/reveal -d '{"id": "testt", "coord":"0,8"}'
echo
curl -H ${JSON} -X POST ${URL}/reveal -d '{"id": "testt", "coord":"7,7"}'
echo
curl -H ${JSON} -X POST ${URL}/reveal -d '{"id": "testt", "coord":"9,6"}'
echo
curl -H ${JSON} -X POST ${URL}/reveal -d '{"id": "testt", "coord":"2,7"}'
echo
curl -H ${JSON} -X POST ${URL}/reveal -d '{"id": "testt", "coord":"0,6"}'
echo
curl -H ${JSON} -X POST ${URL}/reveal -d '{"id": "testt", "coord":"6,9"}'
echo
curl -H ${JSON} -X POST ${URL}/reveal -d '{"id": "testt", "coord":"3,8"}'
echo
curl -H ${JSON} -X POST ${URL}/reveal -d '{"id": "testt", "coord":"8,5"}'
echo
curl -H ${JSON} -X POST ${URL}/reveal -d '{"id": "testt", "coord":"3,9"}'
echo
curl -H ${JSON} -X POST ${URL}/reveal -d '{"id": "testt", "coord":"5,2"}'
echo
curl -H ${JSON} -X POST ${URL}/reveal -d '{"id": "testt", "coord":"6,0"}'
echo -e "\n\n"

sleep 1


echo "Get public end game status"
curl -H ${JSON} -X GET ${URL}
echo -e "\n\n"

sleep 1

echo "Get private end game status"
curl -H ${JSON} -X GET ${URL} -d '{"id": "testt"}'
echo -e "\n\n"

echo "### TEST END ###"
