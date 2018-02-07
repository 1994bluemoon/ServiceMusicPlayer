package vinova.henry.com.servicemusicplayer.feature.home;

public interface IHomeContract {
    interface IView{
        void updateRecyclerView();
    }

    interface IPresenter{
        void getAllMusic();
    }
}
