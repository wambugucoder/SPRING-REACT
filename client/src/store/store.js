import { createStore, applyMiddleware, compose } from "redux";
import thunk from "redux-thunk";
import rootReducer from "./reducers";

const initialState = {};

const middleware = [thunk];
const enhancer=process.env.NODE_ENV==="development"? window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__():compose
const store = createStore(
  rootReducer,
  initialState,
  compose(
    applyMiddleware(...middleware),
    enhancer
    
  
  )
);

export default store;