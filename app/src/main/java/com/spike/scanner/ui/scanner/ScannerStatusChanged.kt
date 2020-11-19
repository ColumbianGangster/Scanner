package com.spike.scanner.ui.scanner

data class ScannerStatusChanged(val origin: Scanner)

enum class Scanner {
    RELOAD,
    SEARCH
}
