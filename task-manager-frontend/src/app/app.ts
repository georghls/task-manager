import { Component, signal } from '@angular/core';
import { TaskDashboard } from './features/tasks/components/task-dashboard/task-dashboard';

@Component({
  selector: 'app-root',
  imports: [TaskDashboard],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('Task Manager');
}
