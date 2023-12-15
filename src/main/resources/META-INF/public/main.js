const objectKeysUl = document.querySelector("#object-keys");
const uiTypesUl = document.querySelector("#ui-types");


const getData = async(elId, resourcePath = 'objectKeys', page =0, size =2) => {
    const page = await fetchListByPage(resourcePath, page, size);
    const ulElement = document.querySelector("#"+elId);
    page.content
    .map( row => JSON.stringify(row))
    .forEach(str => {
        console.log(str)

        // create a new div element
        const liElement = document.createElement("li");

        // and give it some content
        const newContent = document.createTextNode(str);

        // add the text node to the newly created div
        liElement.appendChild(newContent);
        ulElement.appendChild(liElement)
    })

}
const fetchListByPage = async(resourcePath = 'objectKeys', page =0, size =2) => {
return await(await fetch('/'+resourcePath+'/list'+'?page='+page+'&size='+size)).json()
}