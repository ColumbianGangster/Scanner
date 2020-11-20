package com.spike.scanner.ui.zxingscanner

data class ScannerStatusChanged(val origin: Scanner)

enum class Scanner {
    RELOAD,
    SEARCH
}
