package com.eigengraph.egf2.guide

import com.eigengraph.egf2.guide.models.EGF2AdminRole
import com.eigengraph.egf2.guide.models.EGF2User

object DataManager {
	var user: EGF2User? = null
	var followers: Array<String> = emptyArray()
	var admin: EGF2AdminRole? = null
}