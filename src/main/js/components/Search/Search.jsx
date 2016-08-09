import React, { PropTypes } from 'react'

const Component = ({ search }) => (
  <div>
    <input onChange={search} />
  </div>
)

Component.propTypes = {
  search: PropTypes.func.isRequired,
}

export default Component
