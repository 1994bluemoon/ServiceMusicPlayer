package vinova.henry.com.servicemusicplayer.feature.home;

/**
 * Created by dminh on 2/5/2018.
 */

public interface IHomeContract {
    interface IView{
        void updateRecyclerView();
    }

    interface IPresenter{
        void getAllMusic();
    }
}
