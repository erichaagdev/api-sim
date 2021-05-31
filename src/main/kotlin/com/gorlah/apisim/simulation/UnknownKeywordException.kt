package com.gorlah.apisim.simulation

class UnknownKeywordException(keyword: String) : RuntimeException("Unknown keyword '$keyword'")