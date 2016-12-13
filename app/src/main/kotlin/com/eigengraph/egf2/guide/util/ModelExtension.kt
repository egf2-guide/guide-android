package com.eigengraph.egf2.guide.util

import com.eigengraph.egf2.guide.models.commons.HumanName

fun HumanName.fullName(): String = this.given + " " + this.family