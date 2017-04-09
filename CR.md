<style>

p {font-size: 0.8em; text-align: justify;}
.source, .lines {color: #ccc; font-size: 0.5em;}
pre{background-color: #222 !important;}

h1 { font-size: 1.8em !important; }
h2 { font-size: 1.6em !important; }
h3 { font-size: 1.4em !important; }
h4 { font-size: 1.2em !important; }
h5 { font-size: 1.1em !important; }
h6 { font-size: 1.0em !important; }

img[alt=screenshot]{
  display: block;
  margin: auto;
  width: 200px;
  margin: 20px auto 20px auto;
}
</style>

# Compte rendu TP ProductHunt
*ARNOUTS Sylvain, HUBER Bastien, TERRAL Guillaume*

## Introduction

Pour ce TP de conception d'application Android, nous repartons de l'application
codée par Mohammed lors des TP, dont voici une capture d'écran :
![screenshot](./images/Screenshot_begin.png)

## Publications
### Mise en avant de la première publication

Pour mettre en avant la première publication de la liste, il suffit de créer un nouveau layout, différent de celui utilisé pour les items, qui répond aux attentes. Pour cela, on change le layout pour un *linear layout* et on change la largeur de l'image pour *match parent*.

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
  android:orientation="vertical"
  ...
  >
  <ImageView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:adjustViewBounds="true"
    ...
    />
    ...
```

Il faut maintenant faire en sorte de pouvoir affecter ce layout au premier élement de la liste. On va modifier la *ListView* en lui disant qu'il y a deux modes d'affichages possibles. Puis on va renvoyer le premier type si la position est la première, le deuxième sinon.
```java
// File : ui/Adapter/PostAdapter.java
@Override
public int getViewTypeCount() { return 2; }

@Override
public int getItemViewType(int position) {
    return position == 0 ? 0 : 1;
}

@Override
public View getView(int position, View convertView, ViewGroup parent) {
  if(convertView == null) {
    if(position != 0)
      convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent, false);
    else
      convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.big_item,parent, false);
    }
  ...
}
```
Le résultat est conforme.

### Swipe to refresh
On rajoute le support sur Swipe to Refresh sur le layout autour du ListView :
```xml
<android.support.v4.widget.SwipeRefreshLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/swipe_to_refresh_posts"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ListView
        android:id="@+id/listview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</android.support.v4.widget.SwipeRefreshLayout>
```
Et on met en place un listener :
```java
swipeRefreshPosts.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
  @Override
  public void onRefresh() {
    refreshPosts();
    swipeRefreshPosts.setRefreshing(false);
  }
});
```
Le fait d'avoir ajouté la ligne avec `setRefreshing(false)` permet d'arrêter l'icône de rafraichissement. En effet, cette animation rentre en conflit avec l'animation de chargement plein écran déjà mis en place.

### Récupération des dernières publications
On commence par écrire une nouvelle méthode  dans le DAO pour récupérer l'ID du dernier post dans la base de données :
```java
public long getLastPostId() {
  String query = "SELECT "+DataBaseContract.PostTable.ID_COLUMN+" from "+ DataBaseContract.PostTable.TABLE_NAME +" order by "+DataBaseContract.PostTable.ID_COLUMN+" DESC limit 1";
  Cursor c = productHuntDbHelper.getWritableDatabase().rawQuery(query, null);
  if (c != null && c.moveToFirst()) { return c.getLong(0);}
  return 0;
}
```
D'autre part, on modifie le lien pour contacter l'API en ajoutant le numéro de la dernière publication.
```java
if(startIndex != 0) {
  string_url += "&newer="+startIndex;
}
```
Cela implique de modifier le prototype de la méthode `getPostsFromWeb`, qui devient alors :
```java
public static String getPostsFromWeb(Long startIndex) { ... }
```
On peut donc maintenant récupérer uniquement les nouveaux posts.

### Ouverture de la publication dans un navigateur web
Sur la vue détail, on ajoute un menu et un lien de menu.
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:id="@+id/openinbrowser"
        android:title="@string/openinbrowser"
        />
</menu>
```
On met en place le listener qui créera une *Intent*, qui permettra d'ouvrir le lien dans un navigateur du téléphone.
```java
case R.id.openinbrowser:
  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obtainPostUrlFromIntent()));
  startActivity(browserIntent);
  return true;
```
De cette façon, on peut ouvrir un navigateur vers la publication.


### Affichage des commentaires
La récupération et l'affichage des commentaires suit une logique similaire à
celle des publications.
* un modèle ``Comment`` regroupe les propriétés de chaque commentaires
* on spécifie dans le fichier ``DataBaseContract`` la structure de la table
associée
* un ``CommentDao`` permet d'effectuer les opérations de lecture et écriture
 dans la table SQLite
* on définie les opérations ``jsonToComments()`` et ``jsonToComment()`` dans la
class ``JsonParser``

Nous avons fait le choix de récupérer avant tout :
* l'id des commentaires
* leur contenu
* leur date de publications
* le nom, l'username et l'avatar de leur auteur

On accède au fil des commentaires via un click court sur un élément de la listview
de la ``MainActivity``. Un click long amène à l'activité de détail.

Dans la toolbar, un bouton permet de revenir en arrière. Cette action déclenche
aussi le rafraichissement des commentaires.

```java
toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

toolbar.setNavigationOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        refreshComments();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
});
```

Le SwipeToRefresh est implémenté de la même manière que pour les posts.

### Rafraichissement des publications toutes les 2 heures

Pour mettre à jour l'application en background toutes les 2 heures, nous avons
besoin de planifier une alarme. Cette dernière sera de type ELAPSED_REALTIME :
en effet, ce type d'alarme n'est pas affecté par la timezone, et ne réveille
pas l'appareil.

On crée pour cela un *BroadcastReciever*, qui fera appel à la fonction
permettant de raffraichir les publications :
```java
private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d(TAG, "Refreshing listview");
    refreshPosts();
  }
};
```

Dans l'activité principale, on crée alors une fonction *scheduleAlarm*, qui
y fera appel toutes les deux heures. On appelle ensuite cette fonction dans
le onCreate().
```java
public void scheduleAlarm(){

  IntentFilter intentFilter = new IntentFilter(ACTION + ".refreshReceiver");
  registerReceiver(refreshReceiver, intentFilter);

  Intent intent = new Intent(ACTION + ".refreshReceiver");
  alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

  alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
  alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME,
          SystemClock.elapsedRealtime() + 2 * 60 * 60 * 1000,
          2 * 60 * 60 * 1000,
          alarmIntent);
}
```

On veille à bien unregister le BroadcastReceiver en quittant l'application :
```java
@Override
protected void onDestroy() {
  unregisterReceiver(refreshReceiver);
  super.onDestroy();
}
```
