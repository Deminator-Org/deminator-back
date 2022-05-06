function onPageLoad() {
	// Register page elements
	const GUI = {
		settings: {
			i_url: $('#settings-url'),
			b_connect: $('#settings-connect'),
		},
		setup: {
			div: $('#setup-div'),
			i_width: $('#setup-Width'),
			i_height: $('#setup-Height'),
			i_mines: $('#setup-Mines'),
			i_turn: $('#setup-Turn'),
			i_start: $('#setup-Start'),
			i_seed: $('#setup-Seed'),
			b_send: $('#setup-Send'),
		},
		user: {
			div: $('#rename-div'),
			i_id: $('#rename-id'),
			i_name: $('#rename-name'),
			b_send: $('#rename-Send'),
		},
		game: {
			div: $('#game-div'),
			table: $('#game-table'),
			countdown: $('#game-countdown'),
		},
	}

	// Hide everything other than GUI.settings
	GUI.setup.div.hide();
	GUI.user.div.hide();
	GUI.game.div.hide();

	function sendRefreshStatus() {
		// {"id": "testt"}
		const url = GUI.settings.i_url.val() + '/myStatus';
		const payload = JSON.stringify({id: GUI.user.i_id.val()});
		$.ajax(url, {
			type: "POST",
			data: payload,
			success: onReceivePlayerGameData,
			contentType: 'application/json',
			dataType: 'json'
		})
		.fail((data) => {
			console.error(url, payload, data)
		})
	}

	// Setup 'settings' div
	(function() {
		const GUIs = GUI.settings

		function tryConnect() {
			const url = GUI.settings.i_url.val();
			$.get(url, onReceiveGameData)
			.fail((data) => {
				alert('Error while calling (GET) ' + url)
				console.error(url, data)
			})
		}
		GUIs.b_connect.on('click', tryConnect)
	})();

	// Setup 'setup' div
	(function() {
		const GUIs = GUI.setup
		function setMaxMines() {
			const max = (GUI.setup.i_width.val() * GUI.setup.i_height.val() / 2 -1)|0
			if(GUI.setup.i_mines.val() > max) {
				GUI.setup.i_mines.val(max);
			}
			GUI.setup.i_mines.prop('max', max)
		}
		GUIs.i_width.on('change', setMaxMines)
		GUIs.i_height.on('change', setMaxMines)

		function sendSetup() {
			// {"width":10, "height":11, "mines":12, "turnDuration": 10, "startTimeout": 0,"seed":42}
			let seed = GUIs.i_seed.val()
			if(seed === '') {
				seed = Math.random()*(2**32)
			} else if(!/^[0-9]+$/.exec(seed)) {
				seed = seed.hash()
			}

			const payload = JSON.stringify({
				width: GUIs.i_width.val()|0,
				height: GUIs.i_height.val()|0,
				mines: GUIs.i_mines.val()|0,
				turnDuration: GUIs.i_turn.val()|0,
				startTimeout: GUIs.i_start.val()|0,
				seed: seed|0,
			})

			const url = GUI.settings.i_url.val() + '/gameSetup';
			$.ajax(url, {
				type: "POST",
				data: payload,
				success: onReceiveGameData,
				contentType: 'application/json',
				dataType: 'json'
			})
			.fail((data) => {
				alert('Error while calling (POST) ' + url)
				console.error(url, payload, data)
			})
		}
		GUIs.b_send.on('click', sendSetup)
	})();

	// Setup 'rename' div
	(function() {
		const GUIr = GUI.user

		// Set random user ID
		if(!GUIr.i_id.val()) {
			GUIr.i_id.val(generateRandomPlayerId())
		}

		function sendRename() {
			// {"id": "testt", "name":"testt"}

			const payload = JSON.stringify({
				id: GUIr.i_id.val(),
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
			.fail((data) => {
				alert('Error while calling (POST) ' + url)
				console.error(url, payload, data)
			})
		}
		GUIr.b_send.on('click', sendRename)
	}());


	// Prepare response listeners
	let lastReceivedSettings = {}
	function onReceiveGameData(data, inGame=false) {
		// Un-hide game div
		GUI.setup.div.show();
		GUI.user.div.show();
		GUI.game.div.show();


		if(inGame) {
			GUI.game.table.addClass('playable')
			GUI.game.table.on('click', clickOnCell)
		} else {
			GUI.game.table.removeClass('playable')
			GUI.game.table.off('click')
		}

		console.debug('Received GameData', data) // {settings: {...}, players: [{name: }, ...], revealed: {coord: clue, ...}, hasGameEnded: boolean}

		if(data.settings) {
			// update new settings to UI
			if(data.settings.width !== lastReceivedSettings.width) {
				GUI.setup.i_width.val(data.settings.width)
			}
			if(data.settings.height !== lastReceivedSettings.height) {
				GUI.setup.i_height.val(data.settings.height)
			}
			if(data.settings.mines !== lastReceivedSettings.mines) {
				GUI.setup.i_mines.val(data.settings.mines)
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

				// Update countdown until start
				function refreshCountdown() {
					if(!data.settings || !data.settings.startDatetime) {
						GUI.game.countdown.text('Game not started yet')
						return
					}

					let timeUntilStart = (new Date(data.settings.startDatetime) - Date.now())|0
					if(timeUntilStart > 0) {
						GUI.game.countdown.text(msToTime(timeUntilStart))
						setTimeout(refreshCountdown, 100)
					} else {
						GUI.game.countdown.text('Game started')
						// Call to update data
						sendRefreshStatus()
					}
				}
				refreshCountdown()
			}

			lastReceivedSettings = data.settings
		}

		if(data.revealed) {
			for(const cellId of Object.keys(data.revealed)) {
				const clue = data.revealed[cellId]
				const cell = $(`[id="${cellId}"]`)
				cell.addClass('revealed')
				if(clue < 0) {
					cell.addClass('mine')
				} else if(clue > 0) {
					cell.text(clue)
				}
			}
		}
	}

	function onReceivePlayerGameData(data) {
		if(data.player && data.player.revealed) {
			// Add all missing keys from data.player.revealed into data.game.revealed
			for(const cellId of Object.keys(data.player.revealed)) {
				if(!data.game.revealed[cellId]) {
					data.game.revealed[cellId] = data.player.revealed[cellId]
				}
			}
		}

		onReceiveGameData(data.game, !!data.player);

		console.debug('Received PlayerData', data.player) // {name: playerDisplayName, revealed: {coord: clue, ...}}
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
			console.log(target)
			return false; // Already revealed
		}

		const cell = $(target)
		openingCell = true;

		// Set css class "opening" on the cell and on the table
		cell.addClass('opening')
		GUI.game.table.addClass('opening')

		// Send action request to server
		const payload = JSON.stringify({
			id: GUI.user.i_id.val(),
			coord: target.id,
		})
		console.log(payload)

		const url = GUI.settings.i_url.val() + '/reveal';
		$.ajax(url, {
			type: "POST",
			data: payload,
			success: onReceivePlayerGameData,
			contentType: 'application/json',
			dataType: 'json'
		}).fail((data) => {
			alert('Error while calling (POST) ' + url)
			console.error(url, payload, data)
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
	const secondsLeft = seconds - (minutes*60)
	return `${(''+minutes).padStart(2,0)}:${secondsLeft.toFixed(2).padStart(5,0)}`
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
