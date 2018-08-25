package libmanagement

import applib.MyFolder
import applib.MyNote
import applib.MyWord

/**
 * Created by Tanh on 8/11/2017.
 */
class FolderManagement {
    private var listFolder: ArrayList<MyFolder> = ArrayList()
    private var listWord: ArrayList<MyWord> = ArrayList()
    private var listNote: ArrayList<MyNote> = ArrayList()
    private var folderAll: MyFolder= MyFolder("Tất cả")
    var FolderAll: MyFolder
        get() {return folderAll}
        set(value) {
            folderAll= value
        }
    var ListFolder: ArrayList<MyFolder>
        get() {return listFolder}
        set(value) {
            listFolder= value
        }
    constructor()
    constructor(listFolder: ArrayList<MyFolder>, listWord: ArrayList<MyWord>, listNote: ArrayList<MyNote>) {
        this.listFolder = listFolder
        this.listWord = listWord
        this.listNote = listNote
        folderAll.ListWord= listWord
        folderAll.ListNote= listNote
    }
    fun getListWord(myFolder: MyFolder): ArrayList<MyWord>{
        var array: ArrayList<MyWord> = ArrayList()
        for (i in listWord)
            if(i.IdFolder == myFolder.Id)
                array.add(i)
        return array
    }
}