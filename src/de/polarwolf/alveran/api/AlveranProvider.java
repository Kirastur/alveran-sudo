package de.polarwolf.alveran.api;

import de.polarwolf.alveran.orchestrator.AlveranAPI;

public final class AlveranProvider {

	private static AlveranAPI alveranAPI;

	/**
	 * The class is all-static, so prohibit creating an instance
	 */
	private AlveranProvider() {
	}

	/**
	 * Get the Alveran API
	 *
	 * @return API
	 */
	public static AlveranAPI getAPI() {
		return alveranAPI;
	}

	/**
	 * Set the Alveran API
	 *
	 * @param newAlveranAPI new Alveran Instance
	 * @return TRUE if the given Alveran instance could be set for global access,
	 *         else FALSE
	 */
	public static boolean setAPI(AlveranAPI newAlveranAPI) {
		if ((alveranAPI != null) && !alveranAPI.isDisabled()) {
			return false;
		}
		alveranAPI = newAlveranAPI;
		return true;
	}

}
