package com.gorlah.apisim.faker

class UnknownKeywordException(keyword: String) : RuntimeException("Unknown keyword '$keyword'")