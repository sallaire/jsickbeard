import { connect } from 'react-redux'

import Component from './Tvshows'

const mapStateToProps = ({ tvshows }) => ({ tvshows })

export default connect(mapStateToProps)(Component)
