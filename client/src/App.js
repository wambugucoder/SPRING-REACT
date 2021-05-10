import { Provider } from 'react-redux';
import { Route, BrowserRouter as Router } from 'react-router-dom';
import Landing from './components/landing-component/Landing';
import store from './store/store';


function App() {
  return (
    <Provider store={store}>
      <Router>
        <div className="App">
          <Route exact path="/"component={Landing}/>
        </div>
      </Router>

    </Provider>
    
  );
}

export default App;
