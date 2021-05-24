package com.gorlah.apifaker.faker

class UnknownKeywordException(keyword: String) : RuntimeException("Unknown keyword '$keyword'")