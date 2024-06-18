import React, {useEffect, useState} from 'react'
import axios from 'axios';
import TodoForm from './todos/TodoForm'
import TodoLists from './todos/TodoLists'
function Dashboard() {
    const token = sessionStorage.getItem("token");
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
  const BASE_URL = "http://localhost:4000";
  
  const [userTodos, setUserTodos] = useState(null);
  //to get all todos

  const fetchUserTodos = async () => {
    const resp = await axios.get(`${BASE_URL}/api/todo`)

      if (resp.data.todos.length >0) {
        setUserTodos(resp.data.todos);
       
      }
}

useEffect(()=>{

  fetchUserTodos()
  return


}, []);

  
  return (
    <div className='flex flex-col items-center justify-center min-h-full '
    style={{
      backgroundImage: `url('https://cdn.pixabay.com/photo/2018/04/12/18/13/application-3314270_960_720.png')`,
      backgroundSize: 'cover',
      backgroundPosition: 'center',
      width: '100%',
      height: '110vh'
    }}>
 
        <TodoForm fetchUserTodos={fetchUserTodos} BASE_URL={BASE_URL} />
        <TodoLists fetchUserTodos={fetchUserTodos} setUserTodos ={setUserTodos} userTodos={userTodos} BASE_URL={BASE_URL} />
    </div>
  )
}

export default Dashboard