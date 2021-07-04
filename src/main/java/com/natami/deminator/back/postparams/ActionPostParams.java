package com.natami.deminator.back.postparams;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.natami.deminator.back.util.Coord;

public interface ActionPostParams {
	@JsonProperty(value="client", required=true)
	public String getClientID();

	@JsonProperty(value="room", required=true)
	public String getRoomID();

	@JsonProperty(value="opens", required=true)
	public List<Coord> getActionsList();
}
