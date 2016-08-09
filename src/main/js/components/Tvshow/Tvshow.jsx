import React, { PropTypes } from 'react'

const Component = ({ id, name }) => (
  <div>
    <h1>{id}</h1>
    <p>{name}</p>
  </div>
)

Component.propTypes = {
  id: PropTypes.number.isRequired,
  name: PropTypes.string.isRequired,
}

export default Component
