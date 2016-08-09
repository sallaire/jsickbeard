import React, { PropTypes } from 'react'
import Tvshow from '../Tvshow'

const Component = ({ tvshows }) => (
  <div>
    {tvshows.map(tvshow => <Tvshow key={tvshow.id} {...tvshow} />)}
  </div>
)

Component.propTypes = {
  tvshows: PropTypes.array.isRequired,
}

export default Component
