# dprojekt

Extract of an Android application developed by me in the startup I created. This extract is meant to
serve as an example of my coding skills.

ARCHITECTURE:

It is a vertical slice of the presentation and domain layers of the app, including 1 complex
activity (DecDetActivity).
It has been developed following Clean Architecture and Model-View-Presenter pattern.
It uses these libraries:
  - Dagger2: dependency injection (di).
  - ButterKnife: view binding.
  - RxAndroid: reactive components, observable pattern and asynchronous job execution.
  
  
FUNCTIONALITY:

The application is about decisions with multiple participants and multiple options to choose. 
Each participant can give a preference to each option using a custom preferences scale.
The app calculates the best option taking into account every preference of every participant.
Participants can see each preference sent and global results.