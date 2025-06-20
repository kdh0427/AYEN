import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import AchievementList from './AchievementList';
import App from './App'
import ScenarioList from './ScenarioList'
import Start from './Start'
import Signup from './Signup'
import User from './User'
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    {/*<AchievementList />*/}
    {/*<App />*/}
    {/*<ScenarioList />*/}
    {/*<Start />*/}
    {/*<Signup />*/}
    <User />
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
