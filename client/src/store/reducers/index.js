import { combineReducers } from 'redux';
import AuthReducer from './AuthReducer';
import ErrorReducer from './ErrorReducer';

const rootReducer = combineReducers({
    auth:AuthReducer,
    error:ErrorReducer
  
});

export default rootReducer