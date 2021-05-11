import { GET_ERRORS, IS_LOADING } from "../actions/actionTypes";

const INITIAL_STATE = {
    isLoading:false
};
 
// eslint-disable-next-line import/no-anonymous-default-export
export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case GET_ERRORS:
            return action.payload 
            case IS_LOADING:
                return {...state,
                    isRegistered:false,
                    isLoading:true
                }
        default:
            return state
    }
   
}