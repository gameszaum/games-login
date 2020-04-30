package com.gameszaum.login.core.check.api;

import com.gameszaum.login.core.check.Check;
import com.gameszaum.login.core.check.Verify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 *
 * @author netindev
 *
 */
public class Mojang implements Verify {

	private boolean result;

	@Override
	public boolean verify(String playerName) {
		try {
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(new URL(Check.CheckAPI.MOJANG_API.getLink() + playerName).openStream()));
			this.setResult(true);
			return reader.readLine() != null;
		} catch (Exception e) {
			this.setResult(false);
			return false;
		}
	}

	private void setResult(boolean result) {
		this.result = result;
	}

	@Override
	public boolean getResult() {
		return this.result;
	}

}