package com.collectibleArmy.attributes

import org.hexworks.amethyst.api.Attribute

data class Initiative(var forwardInitiative: Int = 0,
                      var retreatInitiative: Int = 0,
                      var attackInitiative: Int = 0,
                      var defendInitiative: Int = 0): Attribute