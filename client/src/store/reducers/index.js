import {combineReducers} from 'redux';
import AuthReducer from './AuthReducer';
import ErrorReducer from './ErrorReducer';
import PollReducer from './PollReducer';

const rootReducer = combineReducers({
  auth : AuthReducer,
  error : ErrorReducer,
  poll : PollReducer

});

export default rootReducer