package com.devkproject.movieinfo

enum class Status {
    RUNNING, SUCCESS, FAILED
}

class NetworkState (val status: Status, val message: String) {

    companion object {
        val LOADED: NetworkState = NetworkState(Status.SUCCESS, "성공")
        val LOADING: NetworkState = NetworkState(Status.RUNNING, "로딩중")
        val ERROR: NetworkState = NetworkState(Status.FAILED, "오류")
        val ENDOFLIST: NetworkState = NetworkState(Status.FAILED, "마지막 페이지")
    }
}