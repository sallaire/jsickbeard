import { connect } from 'react-redux'
import { clearTvshows, fetchTvshows } from '../../redux/actions'
import Component from './Search'

const mapStateToProps = ({ search }) => ({ search })

const mapDispatchToProps = (dispatch) => ({
  search: ({ target: { value } }) => {
    if (value !== '') {
      dispatch(fetchTvshows(value))
    } else {
      dispatch(clearTvshows())
    }
  },
})

export default connect(mapStateToProps, mapDispatchToProps)(Component)
