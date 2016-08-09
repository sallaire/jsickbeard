export const SET_TVSHOWS = 'SET_TVSHOWS'
export const CLEAR_TVSHOWS = 'CLEAR_TVSHOWS'

export const setTvshows = (tvshows) => ({ type: SET_TVSHOWS, tvshows })

export const clearTvshows = () => ({ type: CLEAR_TVSHOWS })

/* eslint-env browser */

export const fetchTvshows = (search) =>
  dispatch => {
    fetch(`http://37.187.19.83/jackbeard/tvshows?name=${search}&lang=fr`,
      { headers: { Authorization: 'Basic YWRtaW46STRtSmFjaw==' } })
      .then(r => r.json())
      .then(tvshows => {
        dispatch(setTvshows(tvshows))
      })
  }
