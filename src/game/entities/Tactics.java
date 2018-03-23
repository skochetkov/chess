package game.entities;

public enum Tactics {
	AGGRESSIVE, // have big advantage in attack with big advantage in defense
	ATTACKING, 	// have advantage in attack with balanced defense
	NEUTRAL, 	// equal play
	DEFENDING,	// opponent has some advantage in attack with balanced defense 
	WEAK, 		// opponent has big advantage in attack and defense
	MODERATE_EXCEPTION,	// near neutral play with king under attack
	CRITICAL_EXCEPTION	// weak position with king under attack
}
