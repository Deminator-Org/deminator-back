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
		},
	}

	// Hide everything other than GUI.settings
	GUI.setup.div.hide();
	GUI.user.div.hide();
	GUI.game.div.hide();

	// Setup 'settings' div
	(function() {
		const GUIs = GUI.settings

		function tryConnect() {
			const url = GUI.settings.i_url.val();
			$.get(url)
				.done(onReceiveGameData)
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

			const payload = {
				width: GUIs.i_width.val()|0,
				height: GUIs.i_height.val()|0,
				mines: GUIs.i_mines.val()|0,
				turnDuration: GUIs.i_turn.val()|0,
				startTimeout: GUIs.i_start.val()|0,
				seed: seed|0,
			}

			const url = GUI.settings.i_url.val() + '/gameSetup';
			$.post(url, payload)
				.done(onReceiveGameData)
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

			const payload = {
				id: GUIr.i_id.val(),
				name: GUIr.i_name.val(),
			}

			const url = GUI.settings.i_url.val() + '/setPlayer';
			$.post(url, payload)
				.done(onReceiveGameData)
				.fail((data) => {
					alert('Error while calling (POST) ' + url)
					console.error(url, payload, data)
				})
		}
		GUIr.b_send.on('click', sendRename)
	}());


	// Prepare response listeners
	let lastReceivedSettings = {}
	function onReceiveGameData(data) {
		// Un-hide game div
		GUI.setup.div.show();
		GUI.user.div.show();
		GUI.game.div.show();

		console.debug('Received GameData', data) // {settings: {...}, players: [{name: }, ...], revealed: {coord: clue, ...}, hasGameEnded: boolean}

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
		lastReceivedSettings = data.settings

		// TODO: Initiate the grid if settings.startDate changed
	}

	function onReceivePlayerGameData(data) {
		onReceiveGameData(data.game);

		console.debug('Received PlayerData', data.player) // {name: playerDisplayName, revealed: {coord: clue, ...}}

		// TODO: Update grid with playerdata information
	}

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
