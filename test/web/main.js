function onPageLoad() {
	// Register page elements
	const GUI = {
		settings: {
			i_url: $('#settings-url'),
			b_connect: $('#settings-connect'),
		},
		setup: {
			div: $('#setup-div'),
			showHide: $('#setup-show'),
			table: $('#setup-table'),
			i_width: $('#setup-Width'),
			i_height: $('#setup-Height'),
			i_mines: $('#setup-Mines'),
			i_turn: $('#setup-Turn'),
			i_start: $('#setup-Start'),
			i_seed: $('#setup-Seed'),
		},
		user: {
			i_name: $('#player-name'),
			b_send: $('#player-send'),
		},
		scores: {
			div: $('#scores-div'),
			table: $('#scores-table'),
		},
		game: {
			div: $('#game-div'),
			table: $('#game-table'),
			countdown: $('#game-countdown'),
		},
	}

	// Hide everything other than GUI.settings
	GUI.setup.div.hide();
	GUI.game.div.hide();
	GUI.scores.div.hide();

	function sendRefreshStatus() {
		// {"id": "testt"}
		const url = GUI.settings.i_url.val() + '/myStatus';
		const payload = JSON.stringify({id: userId});
		$.ajax(url, {
			type: "POST",
			data: payload,
			success: onReceivePlayerGameData,
			error: onReceiveError,
			contentType: 'application/json',
			dataType: 'json'
		})
	}

	// Setup 'settings' div
	(function() {
		const GUIs = GUI.settings

		function tryConnect() {
			const url = GUI.settings.i_url.val();
			$.get(url, onReceiveGameData)
			.fail(onReceiveError)
		}
		GUIs.b_connect.on('click', tryConnect)
	})();

	// Setup 'setup' div
	(function() {
		const GUIs = GUI.setup

		// On click on showHide button, toggle visibility of table
		GUIs.showHide.on('click', function() {
			GUIs.div.dialog({
				resizable: false,
				modal: true,
				width: 'auto',
				buttons: {
					'Start !': sendSetup,
					Cancel: () => GUIs.div.dialog('close'),
				}
			});
		})

		function sendSetup() {
			if(!(validateInput(GUIs.i_width) && validateInput(GUIs.i_height) && validateInput(GUIs.i_mines) && validateInput(GUIs.i_turn) && validateInput(GUIs.i_start))) {
				return;
			}

			// {"width":10, "height":11, "mines":12, "turnDuration": 10, "startTimeout": 0,"seed":42}
			let seed = GUIs.i_seed.val()
			if(seed === '') {
				seed = Math.random()*(2**32)
			} else if(!/^[0-9]+$/.exec(seed)) {
				seed = seed.hash()
			}

			const width = +GUIs.i_width.val()
			const height = +GUIs.i_height.val()
			let mines = (GUIs.i_mines.val()/100 * (width * height))|0
			if(mines < 1) {
				mines = 1
			} else if(mines >= width * height / 2) {
				mines = width * height / 2
			}
			const payload = JSON.stringify({
				width: width,
				height: height,
				mines: mines,
				turnDuration: GUIs.i_turn.val()|0,
				startTimeout: GUIs.i_start.val()|0,
				seed: seed|0,
			})

			const url = GUI.settings.i_url.val() + '/gameSetup';
			$.ajax(url, {
				type: "POST",
				data: payload,
				success: onReceiveGameData,
				error: onReceiveError,
				contentType: 'application/json',
				dataType: 'json'
			})
		}
	})();

	// Setup 'rename' div
	let userId = null;
	(function() {
		const GUIr = GUI.user

		// Set random user ID
		if(!GUIr.i_name.val()) {
			GUIr.i_name.val(generateRandomPlayerId())
		}

		function sendRename() {

			if(!userId) {
				userId = GUIr.i_name.val()
			}

			// {"id": "testt", "name":"testt"}
			const payload = JSON.stringify({
				id: userId,
				name: GUIr.i_name.val(),
			})

			const url = GUI.settings.i_url.val() + '/setPlayer';
			$.ajax(url, {
				type: "POST",
				data: payload,
				success: onReceivePlayerGameData,
				contentType: 'application/json',
				dataType: 'json'
			})
		}
		GUIr.b_send.on('click', sendRename)
	}());



	let countDownTarget = new Date()
	let currentCountdown = null;
	function refreshCountdown() {
		let timeUntilStart = (countDownTarget - Date.now())|0
		if(timeUntilStart > 0) {
			GUI.game.countdown.text(msToTime(timeUntilStart))
			setTimeout(refreshCountdown, 1000)
		} else {
			GUI.game.countdown.text('-')
		}
	}
	function setCountdown(targetDate) {
		if(currentCountdown) {
			clearTimeout(currentCountdown)
		}
		countDownTarget = targetDate
		refreshCountdown()
	}


	// Prepare response listeners
	let lastReceivedSettings = {}
	let nextSyncCall = null
	function onReceiveGameData(data, playerData) {
		console.debug('Received GameData', data) // {settings: {...}, players: [{name: }, ...], revealed: {coord: clue, ...}, hasGameEnded: boolean}

		if(data.settings) {
			GUI.game.div.show();

			// update new settings to UI
			if(data.settings.width !== lastReceivedSettings.width) {
				GUI.setup.i_width.val(data.settings.width)
			}
			if(data.settings.height !== lastReceivedSettings.height) {
				GUI.setup.i_height.val(data.settings.height)
			}
			if(data.settings.mines !== lastReceivedSettings.mines) {
				GUI.setup.i_mines.val(Math.round(data.settings.mines / (data.settings.width * data.settings.height) * 100))
			}
			if(data.settings.turnDuration !== lastReceivedSettings.turnDuration) {
				GUI.setup.i_turn.val(data.settings.turnDuration)
			}

			// TODO: Initiate the grid if settings.startDate changed
			if(data.settings.startDatetime !== lastReceivedSettings.startDatetime) {
				GUI.game.table.empty()
				for(let y = 0; y < data.settings.height; y++) {
					const tr = $('<tr>')
					GUI.game.table.append(tr)
					for(let x = 0; x < data.settings.width; x++) {
						tr.append($(`<td id="${x},${y}" class="cell"></td>`))
					}
				}
			}

			lastReceivedSettings = data.settings
		} else {
			GUI.game.div.hide();
		}

		if(data.revealed) {
			// Clear all cells
			const allCells = GUI.game.table.find('.cell').text('').removeClass('byMe').removeClass('revealed').removeClass('mine')
			for(let i=1; i<=8; i++) {
				allCells.removeClass('v'+i)
			}

			if(playerData && playerData.revealed) {
				for(const cellId of Object.keys(playerData.revealed)) {
					$(`[id="${cellId}"]`).addClass('byMe')
					data.revealed[cellId] = playerData.revealed[cellId]
				}
			}

			for(const cellId in data.revealed) {
				const clue = data.revealed[cellId]
				const cell = $(`[id="${cellId}"]`)
				cell.addClass('revealed')
				if(clue < 0) {
					cell.addClass('mine')
				} else if(clue > 0) {
					cell.text(clue).addClass('v' + clue)
				}
			}
		}

		// Append scores, sorted by score descending, with first column is a Rank
		if(data.players && data.players.length > 0) {
			GUI.scores.div.show();

			data.players.sort((a, b) => b.score - a.score)
			let rankedScore = Infinity
			let rank = 0
			const maxScore = data.players[0].score || 1

			GUI.scores.table.empty();
			for(const player of data.players) {
				if(player.score < rankedScore) {
					rank++;
					rankedScore = player.score;
				}

				const tr = $('<tr>')
				tr.append($(`<td>${rank}</td>`))
				tr.append($(`<td>${player.name}</td>`))

				const pct = (100*player.score/maxScore)|0
				const scoreTd = $(`<td>${player.score}</td>`);
				// Append to scoreTd a progress bar filled at pct% as background style
				scoreTd.css('background-image', `linear-gradient(to right, #FF0 ${pct}%, #FF00 ${pct+1}%)`)
				tr.append(scoreTd)


				GUI.scores.table.append(tr)
			}
		} else {
			GUI.scores.div.hide();
		}

		if(nextSyncCall) {
			clearTimeout(nextSyncCall)
		}
		if(data.nextSync) {
			// Update countdown until start
			setCountdown(new Date(data.nextSync))
			nextSyncCall = setTimeout(sendRefreshStatus, new Date(data.nextSync) - Date.now())
		}
	}

	function onReceivePlayerGameData(data) {
		onReceiveGameData(data.game, data.player);

		const canPlay = data.game.running && data.player && data.player.canPlay;
		if(canPlay) {
			GUI.game.table.addClass('playable')
			GUI.game.table.on('click', clickOnCell)
		} else {
			GUI.game.table.removeClass('playable')
			GUI.game.table.off('click')
		}
	}

	function onReceiveError(data) {
		console.error('Received Error', data)
		alert(data.responseText)
	}

	// Prepare actions
	let openingCell = false
	function clickOnCell(event) {
		if(openingCell) {
			return false;
		}

		// Find the cell we clicked on
		let target = event.target;
		while(!target.classList.contains('cell')) {
			if(target.id === 'game-table') return false; // Not clicking a cell
			target = target.parentElement;
		}
		if(target.classList.contains('revealed')) {
			return false; // Already revealed
		}

		const cell = $(target)
		openingCell = true;

		// Set css class "opening" on the cell and on the table
		cell.addClass('opening')
		GUI.game.table.addClass('opening')

		// Send action request to server
		const payload = JSON.stringify({
			id: userId,
			coord: target.id,
		})
		console.log(payload)

		const url = GUI.settings.i_url.val() + '/reveal';
		$.ajax(url, {
			type: "POST",
			data: payload,
			success: onReceivePlayerGameData,
			error: onReceiveError,
			contentType: 'application/json',
			dataType: 'json'
		}).always(()=>{
			// Reset cell and table styles
			cell.removeClass('opening')
			GUI.game.table.removeClass('opening')

			openingCell = false;
		})
	}

}

function msToTime(ms) {
	const seconds = ms / 1000
	const minutes = (seconds / 60)|0
	const secondsLeft = (seconds - (minutes*60))|0
	return `${(''+minutes).padStart(2,0)}:${(''+secondsLeft).padStart(2,0)}`
}

function generateRandomPlayerId() {
	const A = 65
	const AZ = 26
	const nbLetters = 4
	const nbDigits = 4

	let out = ''
	for(let i=nbLetters; i>0; i--) {
		out += String.fromCharCode(Math.random() * AZ + A)
	}

	out += '-' + ('' + ((Math.random()*(10**nbDigits))|0)).padStart(4, 0)

	return out
}

String.prototype.hash = function() {
	let h=9
	for(let i=0;i<this.length;h=Math.imul(h^this.charCodeAt(i++),9**9));
	return h^h>>>9
}


function validateInput(input) {
	const val = input.val()
	if(input.prop('min') && +val < +input.prop('min')) {
		console.log(input.prop('id'), 'is too small')
		return false;
	}
	if(input.prop('max') && +val > +input.prop('max')) {
		console.log(input.prop('id'), 'is too big')
		return false;
	}
	return true;
}
