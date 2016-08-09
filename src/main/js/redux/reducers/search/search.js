import { SET_SEARCH } from '../../actions'

export const initState = ''
export const initAction = { type: 'INIT_ACTION' }

export default (state = initState, action = initAction) => {
  switch (action.type) {
    case SET_SEARCH:
      return action.search
    default:
      return state
  }
}
