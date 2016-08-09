import { combineReducers } from 'redux'

import tvshows from './tvshows'
import search from './search'

export default combineReducers({
  tvshows,
  search,
})
