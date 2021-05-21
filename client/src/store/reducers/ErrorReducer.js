import { ACTIVATION_ERRORS, ACTIVE_POLL_ERRORS, CLEANUP_ERRORS, GET_ERRORS, IS_LOADING, LOGIN_ERRORS, NON_SCHEDULED_POLL_ERRORS, SCHEDULED_POLL_ERRORS, VOTE_ERRORS } from "../actions/actionTypes";

const INITIAL_STATE = {
    isLoading:false,
    hasErrors:false,
    hasActivationErrors:false,
    hasLoginErrors:false,
    hasNonScheduledPollErrors:false,
    hasScheduledPollErrors:false,
    hasActivePollErrors:false,
    hasVoteErrors:false,
    errorHandler:{}
};
 
// eslint-disable-next-line import/no-anonymous-default-export
export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case GET_ERRORS:
            return {...state,
                isLoading:false,
                errorHandler:action.payload, 
                hasErrors:true
            }
        case ACTIVATION_ERRORS:{
            return{...state,
                isLoading:false,
                hasActivationErrors:true,
                errorHandler:action.payload
            }
        }
        case LOGIN_ERRORS:{
            return{...state,
                isLoading:false,
                hasLoginErrors:true,
                errorHandler:action.payload
            }
        }
        case NON_SCHEDULED_POLL_ERRORS:{
            return{...state,
                isLoading:false,
                hasNonScheduledPollErrors:true,
                errorHandler:action.payload
            }
        } 
        case SCHEDULED_POLL_ERRORS:
            return {...state,
                isLoading:false,
                hasScheduledPollErrors:true,
                errorHandler:action.payload
        
    }     
    case ACTIVE_POLL_ERRORS:
        return {...state,
            isLoading:false,
            hasActivePollErrors:true,
            errorHandler:action.payload
    
}   
    case VOTE_ERRORS:
      return {...state,
        isLoading:false,
        hasVoteErrors:true,
        errorHandler:action.payload

}   
        case IS_LOADING:
                return {...state,
                    isLoading:true
            
        }

         case CLEANUP_ERRORS:
              return{...state,
                isLoading:false,
                hasErrors:false,
                hasActivePollErrors:false,
                hasActivationErrors:false,
                hasLoginErrors:false,
                hasNonScheduledPollErrors:false,
                hasScheduledPollErrors:false,
            }
        default:
            return state
    }
   
}